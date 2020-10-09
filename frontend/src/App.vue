<template>
    <div id="app">
        <b-navbar style="width: 100%" type="dark" variant="dark">
            <b-navbar-brand id="nav-brand" href="#">Flitter</b-navbar-brand>
            <router-link to="/add" class="nav-link text-light">Add flit</router-link>
            <router-link to="/view" class="nav-link text-light">View flits</router-link>
            <router-link to="/users" class="nav-link text-light">View users</router-link>
        </b-navbar>
      <div class="container"></div>
        <div class="row">
            <div class="col s8">
                <router-view></router-view>
            </div>
        </div>
    </div>
</template>

<script>

    export default {
        name: 'app',
        methods: {},
    }
</script>

<style>
    @import 'https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css';
</style>
