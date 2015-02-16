import tweepy
from util import Util
import urllib2
import unirest
from common import Item


class Twitter:

    CONSUMER_KEY = "mvwhsiYQe3UTnnNv888GSXmIy"
    CONSUMER_SECRET = "SfQ6j2bEOURZmzE5fd5oZYs02udxg2RpgAmj3BhF5pFjUyPl9W"
    ACCESS_TOKEN = "2739941347-JFMFrA13BA5w3WQFmKLxm79oGt5IMuGY4BCfEBC"
    ACCESS_SECRET = "BOWclPeXkxqy98DUFu1M1pg4RdgXOWUpfvsMS32Ezu66t"
    TOP_K = 50
    QUERY = "https://loudelement-free-natural-language-processing-service.p.mashape.com/nlp-text/?text="
    HEADERS = {
        "X-Mashape-Key": "WOe6Znx5UpmshoVnevTHgeoYSUfDp1JaNjajsn6CJob9bRSHJd",
        "Accept": "application/json"
    }

    def __init__(self):
        auth = tweepy.OAuthHandler(self.CONSUMER_KEY, self.CONSUMER_SECRET)
        auth.set_access_token(self.ACCESS_TOKEN, self.ACCESS_SECRET)
        self.api = tweepy.API(auth)

    def grab_twitter(self, request):
        # request.form['dist'] is the distance in km.
        all_posts = self.search_tweets(
            request.form['lat'], request.form['long'], request.form['value'])
        return all_posts

    def search_tweets(self, lat, lng, dist):
        gcode = str(lat) + "," + str(lng) + "," + str(dist) + "km"
        public_tweets = self.api.search(geocode=gcode, count=100)
        tags = {}
        tag_to_tweets = {}
        all_tweets = []

        for tweet in public_tweets:
            new_tweet = Util.remove_non_ascii(tweet.text)
            if len(new_tweet) > 0:
                hashtags = Util.extract_hashtags(new_tweet)
                image_urls = []
                image_urls = Util.extract_image_urls(tweet.entities["urls"])

                text = Util.extract_text(new_tweet)
                tweet_object = Item(text, image_urls, hashtags)

                for tag in hashtags:
                    if tag in tags:
                        tags[tag] += 1
                        tag_to_tweets[tag].append(tweet_object)
                    else:
                        tags[tag] = 1
                        tag_to_tweets[tag] = [tweet_object]

        sorted_tags = Util.sort_dict_by_values(tags)

        for i in range(min(self.TOP_K, len(sorted_tags))):
            for tweet in tag_to_tweets[sorted_tags[len(sorted_tags) - 1 - i][0]]:
                response = unirest.get(self.QUERY + urllib2.quote(tweet.text, ""),
                                       headers=self.HEADERS)
                sentiment = response.body["sentiment-text"]
                tweet.sentiment = sentiment

                if sentiment == "positive":
                    tweet.color = "red"
                elif sentiment == "negative":
                    tweet.color = "blue"
                all_tweets.append(tweet)

        return all_tweets

    if __name__ == "__main__":
        lat = 43.665459
        lng = -79.409498
        dist = 0.1
        search_tweets(lat, lng, dist)
