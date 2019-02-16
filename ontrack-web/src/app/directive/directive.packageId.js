angular.module('ot.directive.packageId', [])
    .directive('otPackageId', function () {
        return {
            restrict: 'E',
            templateUrl: 'app/directive/directive.packageId.tpl.html',
            scope: {
                packageId: '='
            }
        };
    })
;