from flask import Flask, request, jsonify, render_template
from twitter_client import Twitter
from instagram_client import Instagram
from util import Util

app = Flask(__name__)
twitter = Twitter()
instagram = Instagram()

@app.route('/')
def index():
  return render_template("index.html")

@app.route('/nearby/trending')
def trending():
  return render_template("trending.html")

@app.route('/nearby', methods=['POST'])
def nearby():

  insta_items = instagram.grab_instagram(request)

  tweet_items = twitter.grab_twitter(request)

  all_items = {'instagram': insta_items,
               'tweet': tweet_items}

  return jsonify(Util.createJSON(all_items))

if __name__ == '__main__':
    app.run(host="127.0.0.1", port=5000, debug=True)
