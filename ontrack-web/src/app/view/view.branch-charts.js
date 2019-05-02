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
                  name
                  creation {
                    time
                  }
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

        // Loads the branch charts data
        function loadBranchCharts() {
            // based on prepared DOM, initialize echarts instance
            let myChart = echarts.init(document.getElementById('charts'));
            // "build" VS data
            let vsBuildData = $scope.branch.builds.map(build => [
                new Date(build.creation.time), // time
                build.validationRuns[0].runInfo.runTime // value
            ]);
            // Specify chart configuration item and data
            let chartOptions = {
                title: {
                    text: 'Build times'
                },
                tooltip: {},
                legend: {},
                xAxis: {
                    type: "time"
                },
                yAxis: {
                    name: "Time (seconds)",
                    nameLocation: "center",
                    nameGap: 30
                },
                series: [{
                    name: 'build',
                    type: 'line',
                    data: vsBuildData
                }]
            };
            // use configuration item and data specified to show chart
            myChart.setOption(chartOptions);
        }

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
                // Loads the charts
                loadBranchCharts();
            });
        }

        // Initial call
        loadBranchData();


    })
;