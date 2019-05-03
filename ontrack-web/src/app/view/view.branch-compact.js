angular.module('ot.view.branch-compact', [
    'ui.router',
    'ot.service.core',
    'ot.service.task',
    'ot.service.form',
    'ot.service.structure',
    'ot.service.buildfilter',
    'ot.service.copy',
    'ot.service.template',
    'ot.dialog.validationStampRunView',
    'ot.dialog.promotionRuns',
    'ot.service.graphql'
])
    .config(function ($stateProvider) {
        $stateProvider.state('branch-compact', {
            url: '/branch-compact/{branchId}',
            templateUrl: 'app/view/view.branch-compact.tpl.html',
            controller: 'BranchCompactCtrl'
        });
    })
    .controller('BranchCompactCtrl', function ($state, $scope, $stateParams, $http, $modal, $location,
                                               ot, otFormService, otStructureService, otAlertService, otTaskService, otNotificationService, otCopyService, otTemplateService,
                                               otBuildFilterService, otGraphqlService) {
        const view = ot.view();

        // Branch's id
        const branchId = $stateParams.branchId;

        // Initial loading taking place... now
        $scope.loadingView = true;

        function loadView() {
            $scope.loadingBuildView = true;
            otGraphqlService.pageGraphQLCall(`query BranchView($branchId: Int!) {
              branches(id: $branchId) {
                id
                name
                project {
                  id
                  name
                }
                otherBranches {
                  id
                  name
                  disabled
                  type
                }
                buildDiffActions {
                  id
                  name
                  type
                  uri
                }
                links {
                  _self
                  _page
                  _reorderValidationStamps
                  _reorderPromotionLevels
                }
                promotionLevels {
                  id
                  name
                  image
                  _image
                  decorations {
                    ...decorationContent
                  }
                }
                validationStamps {
                  id
                  name
                  description
                  image
                  _image
                  decorations {
                    ...decorationContent
                  }
                  dataType {
                    descriptor {
                      id
                      displayName
                    }
                    config
                  }
                }
                builds {
                  id
                  name
                  runInfo {
                    sourceType
                    sourceUri 
                    triggerType
                    triggerData
                    runTime
                  }
                  decorations {
                    ...decorationContent
                  }
                  creation {
                    time
                  }
                  promotionRuns(lastPerLevel: true) {
                    creation {
                      time
                    }
                    promotionLevel {
                      id
                      name
                      image
                      _image
                    }
                    links {
                      _all
                    }
                  }
                  validations {
                    validationStamp {
                      id
                      name
                      dataType {
                        descriptor {
                          id
                        }
                        config
                      }
                    }
                    validationRuns(count: 1) {
                      validationRunStatuses {
                        statusID {
                          id
                          name
                        }
                        description
                        creation {
                          user
                        }
                      }
                    }
                  }
                  links {
                    _validate
                  }
                }
              }
            }
            
            fragment decorationContent on Decoration {
              decorationType
              error
              data
              feature {
                id
              }
            }`, {
                    branchId: branchId
                }
            ).then(function (data) {
                $scope.branch = data.branches[0];
                setupView();
            }).finally(function () {
                $scope.loadingBuildView = false;
            });
        }

        // View setup
        let viewInitialised = false;
        function setupView() {
            if (!viewInitialised) {
                view.breadcrumbs = ot.projectBreadcrumbs($scope.branch.project);
                view.commands = [
                    ot.viewApiCommand($scope.branch.links._self),
                    ot.viewCloseCommand('/branch/' + $scope.branch.id)
                ];
                viewInitialised = true;
            }
        }

        // Initialization
        loadView();

    })
;