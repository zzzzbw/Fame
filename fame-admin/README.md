### 升级问题

1. Webstorm eslint报错

   eslint升级到8.x的时候无法在Webstorm中自动格式化。解决：Webstorm升级到2021.2.2以上或eslint降级到6.x
2. prettier配置报错

    > Error: "prettier/@typescript-eslint" has been merged into "prettier" in eslint-config-prettier 8.0.0. See: https://github.com/prettier/eslint-config-prettier/blob/main/CHANGELOG.md#version-800-2021-02-21
   
    解决：.eslintrc.js中extend去除`prettier/@typescript-eslint`
