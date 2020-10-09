<template>
    <div id="view-tweets">
        <h3>All tweets</h3>
        <ul id="demo">
            <li v-for="tweet in tweets" v-bind:key="tweet">
                <div class="card blue-grey darken-1">
                    <div class="card-content white-text">
                        <span class="card-title">{{tweet.userName}}</span>
                        <p>{{tweet.content}}</p>
                    </div>
                </div>
            </li>
        </ul>
    </div>
</template>

<script>

    import axios from 'axios';

    export default {
        name: 'ViewTweets',
        data() {
            return {
                tweets: []
            }
        },
        methods: {
            load() {
                axios.get('/tweet/list')
                    .then(response => {
                        this.$data.tweets = response.data;
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