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
                    _packageUpload
                }
                packageVersions {
                    packageVersion {
                        packageId {
                            type {
                                id
                                name
                                description
                            }
                            id
                        }
                        version
                    }
                    target {
                        id
                        name
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
    })
;