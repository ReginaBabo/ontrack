angular.module('ot.directive.packageType', [])
    .directive('otPackageType', function () {
        return {
            restrict: 'E',
            templateUrl: 'app/directive/directive.packageType.tpl.html',
            scope: {
                packageType: '='
            }
        };
    })
;