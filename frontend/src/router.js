import Vue from 'vue'
import Router from 'vue-router'

import ViewFlits from "./components/ViewFlits";
import AddFlit from "./components/AddFlit";
import Home from '@/components/Home'
import ViewUsers from "./components/ViewUsers";

Vue.use(Router)

export default new Router({
    mode: 'history',
    routes: [
      {
        path: '/',
        name: 'Home',
        component: Home
      },
      {
        path: '/view',
        name: 'ViewFlits',
        component: ViewFlits
      },
      {
        path: '/users',
        name: 'ViewUsers',
        component: ViewUsers
      },
      {
        path: '/add',
        name: 'AddFlit',
        component: AddFlit
      }
    ]
})