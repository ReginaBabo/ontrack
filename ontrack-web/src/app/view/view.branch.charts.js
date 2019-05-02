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
        $scope.loadingView = true;

    })
;