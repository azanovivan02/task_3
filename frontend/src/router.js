import Vue from 'vue'
import Router from 'vue-router'

import ViewTweets from "./components/ViewTweets";
import AddTweet from "./components/AddTweet";
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
        name: 'ViewTweets',
        component: ViewTweets
      },
      {
        path: '/users',
        name: 'ViewUsers',
        component: ViewUsers
      },
      {
        path: '/add',
        name: 'AddTweet',
        component: AddTweet
      }
    ]
})