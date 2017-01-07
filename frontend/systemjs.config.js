/**
 * System configuration for Angular 2 samples
 * Adjust as necessary for your application needs.
 */
(function(global) {
  var paths = {
    // paths serve as alias
    'npm:':                       'node_modules/'
  };
  // map tells the System loader where to look for things
  var map = {
    'app':                        'dist',
    '@angular':                   'node_modules/@angular',
    'ng2-uploader':               'node_modules/ng2-uploader',
    'angular-in-memory-web-api':  'node_modules/angular-in-memory-web-api',
    'd3':                         'node_modules/d3',
    'rxjs':                       'node_modules/rxjs',
    'moment':                     'node_modules/moment',
    'ng2-bootstrap':              'node_modules/ng2-bootstrap',
  };
  // packages tells the System loader how to load when no filename and/or no extension
  var packages = {
    'app':                        { main: 'main.js',  defaultExtension: 'js' },
    'ng2-uploader':               { defaultExtension: 'js' },
    'rxjs':                       { defaultExtension: 'js' },
    'angular-in-memory-web-api':  { main: 'index.js', defaultExtension: 'js' },
    'd3':                         { main: 'd3.js', defaultExtension: 'js' },
    'ng2-bootstrap':              { format: 'cjs', main: 'bundles/ng2-bootstrap.umd.js', defaultExtension: 'js' },
    'moment':                     { main: 'moment.js', defaultExtension: 'js' },
  };
  var ngPackageNames = [
    'common',
    'compiler',
    'core',
    'forms',
    'http',
    'platform-browser',
    'platform-browser-dynamic',
    'router',
    'router-deprecated',
    'upgrade',
  ];
  // Individual files (~300 requests):
  function packIndex(pkgName) {
    packages['@angular/'+pkgName] = { main: 'index.js', defaultExtension: 'js' };
  }
  // Bundled (~40 requests):
  function packUmd(pkgName) {
    packages['@angular/'+pkgName] = { main: 'bundles/' + pkgName + '.umd.js', defaultExtension: 'js' };
  }
  // Most environments should use UMD; some (Karma) need the individual index files
  var setPackageConfig = System.packageWithIndex ? packIndex : packUmd;
  // Add package entries for angular packages
  ngPackageNames.forEach(setPackageConfig);
  var config = {
    paths: paths,
    map: map,
    packages: packages
  };
  System.config(config);
})(this);
