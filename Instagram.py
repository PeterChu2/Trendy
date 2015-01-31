from instagram.client import InstagramAPI
from flask import Flask, request, jsonify, render_template
from util import Util
from twitter import Item
import twitter

app = Flask(__name__)

access_token = "YOUR_ACCESS_TOKEN"
api = InstagramAPI(client_id='5d9cdd5e12dd4ed7862ff7d0b9867bd7', client_secret='ba7221fefb2547f291af2a98d30b768a')

@app.route('/nearby', methods=['POST'])
def nearby():
  #try:
  #  media_search = api.media_search( count=10, lat=request.form['lat'], lng=request.form['long'], distance=10 )
  #  if(media_search):
  #    items = createJSON(media_search)
      #print items
  #    return jsonify(items)
  #except UnicodeEncodeError:
  #  pass #NOOP

  insta_items = grab_instagram(request)
  tweet_items = grab_twitter(request)

  all_items = insta_items + tweet_items
  
  return jsonify(createJSON(all_items))

def grab_twitter(request):
  #request.form['dist'] is the distance in km.
  all_posts = twitter.search_tweets(request.form['lat'], request.form['long'], request.form['dist'])
  
  return all_posts

""" Return a list of Item."""
def grab_instagram(request):
  all_posts = []

  try:
    media_search = api.media_search( count=10, lat=request.form['lat'], lng=request.form['long'], distance=10 )

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
  i = 0
  all_tags = set()

  for item in items:
    json_item[i] = item.serialize()
    i += 1
    tag_set = set(item.tags)
    all_tags = all_tags.union(tag_set)
  json_item['tags'] = list(all_tags)

  return json_item 

if __name__ == '__main__':
    app.run(host="127.0.0.1", port=5000, debug=True)
