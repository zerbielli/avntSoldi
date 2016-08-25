/**
 * This file contains mainModule with injecting dependencies, routing and configuration.
 *
 * @version 1.1
 * @author Dmitry
 * @since 22.01.2016
 */
angular.module("mainModule", ['gettext', 'ui.utils', 'ui.router', 'angularMoment', 'ngMaterial', 'md.data.table',
    'angular-loading-bar', 'templates', 'angular-cache', 'duScroll', 'ngFileUpload', 'vcRecaptcha',
    'ngFileSaver'])
    .value('duScrollDuration', 3000)
    .config(['$stateProvider', '$urlRouterProvider', '$httpProvider', '$locationProvider',
        function($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider){
            $locationProvider.html5Mode(true);

            $stateProvider
                .state('menu', {
                    url: "/menu"
                })
                .state('cheque', {
                    abstract: true,
                    url: "/cheques",
                    views: {
                        "header": {
                            controller:
                                ['$rootScope', '$scope', '$state', function($rootScope, $scope, $state) {
                                    $scope.selectedTab = $state.current.data.selectedTab;

                                    $rootScope.$on('$stateChangeSuccess',
                                        function(event, toState, toParams, fromState, fromParams) {
                                            $scope.selectedTab = toState.data.selectedTab;
                                        });
                                }],
                            template: '<header selected-tab="selectedTab"></header>'
                        },
                        "content": {
                            template: "<ui-view></ui-view>"
                        }
                    }
                })
                .state('cheque.filter', {
                    url: "^/filter",
                    template: "<filters-page></filters-page>",
                    data: {'selectedTab': 0}
                })
                .state('cheque.edit', {
                    url: "^/cheque/{chequeId:[0-9]{1,10}}",
                    controller:
                        ['$scope', '$stateParams', function($scope, $stateParams) {
                            $scope.chequeID = $stateParams.chequeId;
                        }],
                    template: '<cheque-page cheque-id="chequeID"></cheque-page>',
                    data: {'selectedTab': 1}
                })
                .state('cheque.create', {
                    url: "^/cheque",
                    template: '<cheque-page></cheque-page>',
                    data: {'selectedTab': 1}
                })
                .state('cheque.dashboard', {
                    url: "^/dashboard",
                    template: '<dashboard></dashboard>',
                    data: {'selectedTab': 2, 'access':['ROLE_ADMIN', 'ROLE_BOSS', 'ROLE_ENGINEER']}
                })
                .state('cheque.analytics', {
                    url: "^/analytics",
                    template: '<analytics-page></analytics-page>',
                    data: {'selectedTab': 3, 'access':['ROLE_ADMIN', 'ROLE_BOSS']}
                })
                .state('cheque.profile', {
                    url: "^/profile",
                    template: '<profile-page></profile-page>',
                    data: {'selectedTab': 4, 'access':['ROLE_ADMIN']}
                })
                .state('cheque.login', {
                    url: "^/login",
                    template: '<login-page></login-page>',
                    data: {'selectedTab': 5}
                });

            $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

            $httpProvider.interceptors.push(
                ['$q', '$injector', function($q, $injector) {
                    return {
                        responseError: function (response) {

                            if(response.status === -1) {
                                var warning = $injector.get('warning');
                                warning.showServerConnectionLostException();
                                return $q.reject(response);
                            }

                            if(response.status === 403) {
                                var $http = $injector.get('$http');
                                var deferred = $q.defer();
                                $http.get('/api/test').then(deferred.resolve, deferred.reject);
                                return deferred.promise.then(function () {
                                    return $http(response.config);
                                });
                            }

                            if(response.data.exception === "org.springframework.orm.ObjectOptimisticLockingFailureException") {
                                var warning = $injector.get('warning');
                                warning.showAlertOptimisticLockingException();
                                return $q.reject(response);
                            }

                            return $q.reject(response);
                        }
                    };
                }]
            );
    }])
    .config(['$mdThemingProvider', function($mdThemingProvider){
            $mdThemingProvider
                .theme('default')
                .primaryPalette('blue', {'default':'500'})
                .accentPalette('teal', {'default':'500'});

            $mdThemingProvider
                .theme('inner-block')
                .primaryPalette('blue-grey', {'default':'50'});

            $mdThemingProvider
                .theme('header')
                .primaryPalette('indigo', {'default':'500'})
                .accentPalette('grey', {'default':'200'});
//                .dark();
    }])
    .config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
        cfpLoadingBarProvider.includeSpinner = false;
        cfpLoadingBarProvider.latencyThreshold = 100;
    }])
    .config(['$mdDateLocaleProvider',
        function($mdDateLocaleProvider) {
            $mdDateLocaleProvider.months = 'Janeiro_Fevereiro_Março_Abril_Maio_Junho_Julho_Agosto_Setembro_Outubro_Novembro_Dezembro'.split('_');
            $mdDateLocaleProvider.shortMonths = 'Jan_Fev_Mar_Abr_Mai_Jun_Jul_Ago_Set_Out_Nov_Dez'.split('_');
            $mdDateLocaleProvider.days = 'Domingo_Segunda_Terça_Quarta_Quinta_Sexta_Sábado'.split('_');
            $mdDateLocaleProvider.shortDays = 'Dom_Seg_Ter_Qua_Qui_Sex_Sáb'.split('_');
            // Change week display to start on Monday.
            $mdDateLocaleProvider.firstDayOfWeek = 1;
    }])
    .run(['gettextCatalog', 'amMoment', 'auth', 'security', 'autocomplete', '$rootScope',
        function(gettextCatalog, amMoment, auth, security, autocomplete, $rootScope){
            auth.init();
            security.init();
            gettextCatalog.setCurrentLanguage('pt');
            amMoment.changeLocale('pt');
            autocomplete.getDataFromServer('users');
            $rootScope.tableFilter = {order: '-id', limit: 15, page: 1};
    }]);
