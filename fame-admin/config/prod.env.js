'use strict'
const BASE_URL = process.argv.splice(2)[0]
module.exports = {
  BASE_URL: BASE_URL ? `"` + BASE_URL + `"` : undefined,
  NODE_ENV: '"production"'
}

