from instagram.client import InstagramAPI
from flask import Flask, request, jsonify, render_template
from util import Util
from twitter import Item
import twitter

app = Flask(__name__)

access_token = "YOUR_ACCESS_TOKEN"
api = InstagramAPI(client_id='5d9cdd5e12dd4ed7862ff7d0b9867bd7', client_secret='ba7221fefb2547f291af2a98d30b768a')

@app.route('/')
def index():
  return render_template("index.html")

@app.route('/nearby/trending')
def trending():
  return render_template("trending.html")

@app.route('/nearby', methods=['POST'])
def nearby():

  insta_items = grab_instagram(request)

  tweet_items = grab_twitter(request)

  all_items = {'instagram': insta_items,
               'tweet': tweet_items}


  return jsonify(createJSON(all_items))

def grab_twitter(request):
  #request.form['dist'] is the distance in km.
  all_posts = twitter.search_tweets(request.form['lat'], request.form['long'], request.form['value'])

  return all_posts

""" Return a list of Item."""
def grab_instagram(request):
  all_posts = []

  try:
    media_search = api.media_search( count=10, lat=request.form['lat'], lng=request.form['long'], distance=request.form['value'] * 1000 )

    if(media_search):
      for media in media_search:
        if ( hasattr(media, 'images') & ("standard_resolution" in media.images) ):
          img_urls = [media.images['standard_resolution'].url]

        if hasattr(media.caption, 'text'):
          text = Util.remove_non_ascii(media.caption.text)
        else:
          text = ""

        tags = Util.extract_hashtags(text)

        item = Item(text, img_urls, tags)

        all_posts.append(item)

  except UnicodeEncodeError:
    pass #NOOP

  return all_posts

def createJSON(items):
  json_item = {}

  for key in items:
    i = 0
    all_tags = set()
    json_item[key] = {}

    for item in items[key]:
      json_item[key][i] = item.serialize()
      i += 1
      tag_set = set(item.tags)
      all_tags = all_tags.union(tag_set)

    json_item[key]['tags'] = list(all_tags)
    json_item[key]['num'] = i

  return json_item

if __name__ == '__main__':
    app.run(host="127.0.0.1", port=5000, debug=True)
