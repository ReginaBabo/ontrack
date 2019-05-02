angular.module('ot.view.branch.charts', [
    'ui.router',
    'ot.service.core',
    'ot.service.graphql'
])
    .config(function ($stateProvider) {
        $stateProvider.state('branch-charts', {
            url: '/branch-charts/{branchId}',
            templateUrl: 'app/view/view.branch-charts.tpl.html',
            controller: 'BranchChartsCtrl'
        });
    })
    .controller('BranchChartsCtrl', function ($state, $scope, $stateParams, $http,
                                              ot, otGraphqlService) {
        const view = ot.view();
        // Branch's id
        const branchId = $stateParams.branchId;

        // Initial loading taking place... now
        let viewInitialized = false;
        $scope.loadingView = true;

        // Branch query variables
        const queryVariables = {
              branchId: branchId
        };

        // Branch query
        const query = `
            query BranchChart($branchId: Int!) {
              branches(id: $branchId) {
                id
                name
                project {
                  id
                  name
                }
                builds(filter: {afterDate: "2018-07-01"}) {
                  id
                  validationRuns(count: 1, validationStamp: "build") {
                    creation {
                      time
                    }
                    validationRunStatuses {
                      statusID {
                        id
                      }
                    }
                    runInfo {
                      runTime
                    }
                  }
                }
              }
            }
        `;

        // Loads the branch
        function loadBranchData() {
            otGraphqlService.pageGraphQLCall(query, queryVariables).then(function (data) {
                $scope.branch = data.branches[0];
                // View initialization
                if (!viewInitialized) {
                    // View title
                    view.breadcrumbs = ot.branchBreadcrumbs($scope.branch);
                    // Commands
                    view.commands = [
                        ot.viewCloseCommand('/branch/' + $scope.branch.id)
                    ];
                    // View OK now
                    viewInitialized = true;
                }
            });
        }

        // Initial call
        loadBranchData();


    })
;