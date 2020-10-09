<template>
    <div id="view-flits">
        <h3>All flits</h3>
        <ul id="demo">
            <li v-for="flit in flits" v-bind:key="flit">
                <div class="card blue-grey darken-1">
                    <div class="card-content white-text">
                        <span class="card-title">{{flit.userName}}</span>
                        <p>{{flit.content}}</p>
                    </div>
                </div>
            </li>
        </ul>
    </div>
</template>

<script>

    import axios from 'axios';

    export default {
        name: 'ViewFlits',
        data() {
            return {
                flits: []
            }
        },
        methods: {
            load() {
                axios.get('/flit/list')
                    .then(response => {
                        this.$data.flits = response.data;
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