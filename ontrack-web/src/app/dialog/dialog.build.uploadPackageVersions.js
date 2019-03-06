angular.module('ot.dialog.build.uploadPackageVersions', [
    'ot.service.core',
    'ot.service.form'
])
    .controller('otDialogBuildUploadPackageVersions', function ($scope, $modalInstance, $http, config, ot, otFormService) {
        // Inject the configuration into the scope
        $scope.config = config;
        $scope.parsers = config.parsers;
        $scope.packageTypes = config.packageTypes;
        $scope.build = config.build;
        // Cancelling the dialog
        $scope.cancel = () => {
            $modalInstance.dismiss('cancel');
        };
        // Submitting the dialog
        $scope.submit = isValid => {
            if (isValid) {
                otFormService.submitDialog(
                    config.submit,
                    $scope.xxxx,
                    $modalInstance,
                    $scope
                );
            }
        };
    })
;