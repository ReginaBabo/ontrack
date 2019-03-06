angular.module('ot.view.build-packages', [
    'ui.router',
    'ot.service.core',
    'ot.service.structure',
    'ot.service.graphql',
    'ot.dialog.build.uploadPackageVersions'
])
    .config(function ($stateProvider) {
        $stateProvider.state('buildPackages', {
            url: '/build-packages/{buildId}',
            templateUrl: 'app/view/view.build-packages.tpl.html',
            controller: 'BuildPackagesCtrl'
        });
    })
    .controller('BuildPackagesCtrl', function ($modal, $state, $scope, $stateParams, $http, ot, otGraphqlService) {
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
                // Gets the list of MIME types
                const parserQuery = `
                    {
                      buildPackageVersionParsers {
                        name
                        mimeType
                        description
                        example
                      }
                      packageTypes {
                        id
                        name
                      }
                    }
                `;
                otGraphqlService.pageGraphQLCall(parserQuery).then(data => {
                    return $modal.open({
                        templateUrl: 'app/dialog/dialog.build.uploadPackageVersions.tpl.html',
                        controller: 'otDialogBuildUploadPackageVersions',
                        resolve: {
                            config: () => ({
                                parsers: data.buildPackageVersionParsers,
                                packageTypes: data.packageTypes,
                                build: $scope.build,
                                submit: function (dialogData) {
                                    const queryParams = {};
                                    if (dialogData.packageType) {
                                        queryParams.defaultTypeName = dialogData.packageType.id;
                                    }
                                    return ot.pageCall($http.post(
                                        $scope.build.links._packageUploadAsText,
                                        dialogData.content,
                                        {
                                            params: queryParams,
                                            headers: {
                                                "Content-Type": dialogData.parser.mimeType
                                            }
                                        }
                                    ));
                                }
                            })
                        }
                    }).result;
                }).then(loadPackageVersions);
                // TODO On OK, POST the text, waiting time
            }
        };
    })
;