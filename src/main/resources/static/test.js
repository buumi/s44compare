angular
    .module('app', ['nvd3', 'ui.bootstrap'])

    .controller('MainCtrl', ['$http', '$scope', function($http, $scope) {

        var controller = this;

        $scope.selectedMode = "TABLE";

        $scope.unitNames = [];

        $scope.units = [];

        $scope.weaponVariables = [];
        $scope.unitVariables = [];

        $scope.selectedUnits = [];

        $scope.onUnitClicked = function ($item, $model, $label) {
            $scope.selectedUnits.push(controller.selectUnit($item));

            controller.updateChart();
        };

        this.selectUnit = function (name) {
            for (var i = 0 ; i < $scope.units.length; i++) {
                if ($scope.units[i].name == name) {
                    return $scope.units[i];
                }
            }
        };

        this.getWeapons = function (unit) {
            var weapons = [];

            for (i = 0; i < unit.weapons.length ; i++) {
                weapons.push(unit.weapons[i]);
            }

            return weapons;
        };

        $scope.switchToChart = function () {
            $scope.selectedMode = "CHART";

            $scope.api.updateWithTimeout(200);
            $scope.api2.updateWithTimeout(200);
            $scope.api3.updateWithTimeout(200);
        };

        $http.get("/s44compare/unitnames").success(function (data) {
            $scope.unitNames = data;
        });

        $http.get("/s44compare/units").success(function(data) {
            $scope.units = data;
        });

        $http.get("/s44compare/interestingvalues").success(function(data) {
            $scope.unitVariables = data.Units;
            $scope.weaponVariables = data.Weapons;
        });

        this.updateChart = function () {
            var data = [];

            for (i = 0 ; i < $scope.selectedUnits.length ; i++) {
                unit = $scope.selectedUnits[i];

                dataObject = {};

                dataObject.key = unit.name;
                dataObject.values = [];

                keys = Object.keys(unit.attributes);

                for (j = 0 ; j < keys.length ; j++) {
                    valueObject = {};
                    valueObject.value = parseFloat(unit.attributes[keys[j]]);
                    valueObject.label = keys[j];

                    dataObject.values.push(valueObject);
                }

                data.push(dataObject);
            }

            $scope.mainChartData = data;

            this.updateWeaponsChart();
            this.updateWeaponDamagesChart();
        };

        this.updateWeaponsChart = function () {
            var data = [];

            for (i = 0 ; i < $scope.selectedUnits.length ; i++) {
                unit = $scope.selectedUnits[i];

                for (j = 0; j < unit.weapons.length; j++) {
                    weapon = unit.weapons[j];

                    dataObject = {};

                    dataObject.key = weapon.name + " (" + unit.name + ")";
                    dataObject.values = [];

                    keys = Object.keys(weapon.weaponAttributes);

                    for (k = 0 ; k < keys.length ; k++) {
                        valueObject = {};
                        valueObject.value = parseFloat(weapon.weaponAttributes[keys[k]]);
                        valueObject.label = keys[k];

                        dataObject.values.push(valueObject);
                    }

                    data.push(dataObject);
                }
            }

            $scope.weaponsChartData = data;
        };

        this.updateWeaponDamagesChart = function () {
            var data = [];

            for (i = 0 ; i < $scope.selectedUnits.length ; i++) {
                unit = $scope.selectedUnits[i];

                for (j = 0; j < unit.weapons.length; j++) {
                    weapon = unit.weapons[j];

                    damageGroups = Object.keys(weapon.damages);

                    dataObject = {};
                    dataObject.key = weapon.name + " (" + unit.name + ")";
                    dataObject.values = [];

                    for (k = 0; k < damageGroups.length ; k++) {
                        valueObject = {};
                        valueObject.value = parseFloat(weapon.damages[damageGroups[k]]);
                        valueObject.label = "Damages " + damageGroups[k];

                        dataObject.values.push(valueObject);
                    }

                    data.push(dataObject);
                }
            }

            $scope.weaponsDamageChartData = data;
        };

        $scope.mainChartData = [];
        $scope.weaponsChartData = [];
        $scope.weaponsDamageChartData = [];

        $scope.mainChartOptions = {
            chart: {
                type: 'multiBarChart',
                height: 450,
                margin : {
                    top: 20,
                    right: 20,
                    bottom: 50,
                    left: 55
                },
                stacked: false,
                showControls: false,
                x: function(d){return d.label;},
                y: function(d){return d.value;},
                showValues: true,
                valueFormat: function(d){
                    return d3.format(',.5f')(d);
                },
                tooltip: {
                    contentGenerator: function (e) {
                        var series = e.series[0];
                        if (series.value === null) return;

                        var rows =
                            "<tr>" +
                            "<td class='key'>" + 'Attribute: ' + "</td>" +
                            "<td class='x-value'>" + e.value + "</td>" +
                            "</tr>" +
                            "<tr>" +
                            "<td class='key'>" + 'Value: ' + "</td>" +
                            "<td class='x-value'><strong>" + (series.value?series.value.toFixed(4):0) + "</strong></td>" +
                            "</tr>";

                        var header =
                            "<thead>" +
                            "<tr>" +
                            "<td class='legend-color-guide'><div style='background-color: " + series.color + ";'></div></td>" +
                            "<td class='key'><strong>" + series.key + "</strong></td>" +
                            "</tr>" +
                            "</thead>";

                        return "<table>" +
                            header +
                            "<tbody>" +
                            rows +
                            "</tbody>" +
                            "</table>";
                    }
                },
                duration: 500,
                xAxis: {
                    axisLabel: 'Attribute'
                },
                yAxis: {
                    axisLabel: 'Value',
                    axisLabelDistance: -10
                }
            }
        };

    }]);