(function() {
    'use strict';

    angular
        .module('gestCApp')
        .config(bootstrapMaterialDesignConfig);

    /*compileServiceConfig.$inject = [];*/bootstrapMaterialDesignConfig.$inject = [];

    function bootstrapMaterialDesignConfig() {
        $.material.init();

    }
})();
