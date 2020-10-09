<template>
    <div id="view-users">
        <h3>All users</h3>
        <ul id="demo">
            <li v-for="user in users" v-bind:key="user">
                <div class="card white">
                    <div class="card-content text-black-50">
                        <span class="card-title">{{user}}</span>
                    </div>
                </div>
            </li>
        </ul>
    </div>
</template>

<script>

    import axios from 'axios';

    export default {
        name: 'ViewUsers.vue',
        data() {
            return {
                users: []
            }
        },
        methods: {
            load() {
                axios.get('/user/list')
                    .then(response => {
                        this.$data.users = response.data;
                    })
                    .catch(error => {
                        console.log('ERROR: ' + error.response.data);
                    })
            }
        },
        mounted() {
            this.load();
        }
    }
</script>

<style>
</style>