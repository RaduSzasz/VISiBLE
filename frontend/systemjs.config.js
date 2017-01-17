(function (global) {
  var paths = {
    'npm:': 'node_modules/'
  };

  var maps = {
    // our app is within the app folder
    app: 'dist',

    // angular bundles
    '@angular':      'npm:@angular',
    'ng2-uploader':  'npm:ng2-uploader',
    'ng2-bootstrap': 'npm:ng2-bootstrap',

    // other libraries
    'd3':            'npm:d3',
    'rxjs':          'npm:rxjs',
    'moment':        'npm:moment',
  };

  var packages = {
    'app': {
      main: './main.js',
      defaultExtension: 'js'
    },
    'rxjs': {
      defaultExtension: 'js'
    },
    'ng2-uploader': {
      main: 'index.js',
      defaultExtension: 'js'
    },
    'd3': {
      main: 'd3.js',
      defaultExtension: 'js'
    },
    'ng2-bootstrap': {
      format: 'cjs',
      main: 'bundles/ng2-bootstrap.umd.js',
      defaultExtension: 'js'
    },
    'moment': {
      main: 'moment.js',
      defaultExtension: 'js'
    },
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

  function packIndex(pkgName) {
    packages['@angular/'+pkgName] = { main: 'index.js', defaultExtension: 'js' };
  }
  // Bundled (~40 requests):
  function packUmd(pkgName) {
    packages['@angular/'+pkgName] = { main: '/bundles/' + pkgName + '.umd.js', defaultExtension: 'js' };
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
