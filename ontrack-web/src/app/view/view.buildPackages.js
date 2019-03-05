angular.module('ot.view.build-packages', [
    'ui.router',
    'ot.service.core',
    'ot.service.structure',
    'ot.service.graphql'
])
    .config(function ($stateProvider) {
        $stateProvider.state('buildPackages', {
            url: '/build-packages/{buildId}',
            templateUrl: 'app/view/view.build-packages.tpl.html',
            controller: 'BuildPackagesCtrl'
        });
    })
    .controller('BuildPackagesCtrl', function ($state, $scope, $stateParams, $http, ot, otGraphqlService) {
        const view = ot.view();
        // Build's id
        const queryParams = {
            buildId: $stateParams.buildId,
        };
        // GraphQL query
        const query = `
            query Build($buildId: Int!) {
              builds(id: $buildId) {
                id
                name
                branch {
                  id
                  name
                  project {
                    id
                    name
                  }
                }
                links {
                    _packageUploadAsText
                }
                packageVersions {
                    packageVersion {
                        packageId {
                            type {
                                id
                                name
                                description
                                feature {
                                    id
                                }
                            }
                            id
                        }
                        version
                    }
                    target {
                        id
                        name
                        branch {
                          name
                          links {
                            _page
                          }
                          project {
                            name
                            links {
                              _page
                            }
                          }
                        }
                        links {
                          _page
                        }
                        promotionRuns(lastPerLevel: true) {
                          promotionLevel {
                            id
                            name
                            image
                            _image
                          }
                        }
                    }
                }
              }
            }
        `;
        // Loading function
        function loadPackageVersions() {
            otGraphqlService.pageGraphQLCall(query, queryParams).then(data => {
                const build = data.builds[0];
                $scope.build = build;
                // View configuration
                view.breadcrumbs = ot.buildBreadcrumbs(build);
            });
        }
        // Initial call
        loadPackageVersions();

        /**
         * Uploading package versions for this build
         */
        $scope.uploadAsText = () => {
            if ($scope.build.links._packageUploadAsText) {
                // TODO Gets the list of MIME types
                // TODO Modal dialog with two fields
                // TODO i) MIME type
                // TODO ii) Text (limited to 10000 characters)
                // TODO On OK, POST the text, waiting time
                // TODO On upload OK, refresh the list of items (callback to loadPackageVersions)
            }
        };
    })
;