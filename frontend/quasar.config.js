const { configure } = require('quasar/wrappers');

module.exports = configure(() => {
  return {
    css: ['app.css'],
    boot: ['axios'],
    extras: ['roboto-font', 'material-icons'],
    framework: {
      config: {}
    },
    build: {
      target: {
        browser: ['es2020'],
        node: 'node20'
      },
      vueRouterMode: 'history'
    },
    devServer: {
      open: false
    },
    capacitor: {
      hideSplashscreen: true
    }
  };
});
