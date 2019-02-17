angular.module('ot.dialog.project.packageIds', [
    'ot.service.core',
    'ot.service.form'
])
    .controller('otDialogProjectPackageIds', function ($scope, $modalInstance, $http, config, ot, otFormService) {
        // Inject the configuration into the scope
        $scope.config = config;
        $scope.packageTypes = config.packageTypes;
        $scope.packageIds = config.project.packageIds;
        // Edition
        $scope.package = {
            type: "",
            id: ""
        };
        $scope.selectedPackageId = undefined;
        // Selects a package ID for edition
        $scope.editPackageId = (packageId) => {
            $scope.package.type = packageId.type.id;
            $scope.package.id = packageId.id;
            $scope.selectedPackageId = packageId;
        };
        // Clears selection
        $scope.clearPackageId = () => {
            $scope.package.type = "";
            $scope.package.id = "";
            $scope.selectedPackageId = undefined;
        };
        // Saves edition
        $scope.savePackageId = () => {
            if ($scope.package.id && $scope.package.type) {
                if ($scope.selectedPackageId) {
                    $scope.selectedPackageId.type = $scope.packageTypes.find(it => it.id === $scope.package.type);
                    $scope.selectedPackageId.id = $scope.package.id;
                    $scope.clearPackageId();
                } else {

                }
            }
        };
        // Cancelling the dialog
        $scope.cancel = () => {
            $modalInstance.dismiss('cancel');
        };
        // Submitting the dialog
        $scope.submit = isValid => {
            if (isValid) {
                otFormService.submitDialog(
                    config.submit,
                    $scope.data,
                    $modalInstance,
                    $scope
                );
            }
        };
    })
;