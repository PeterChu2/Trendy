from flask import Flask, request, jsonify, render_template
from util import Util
from Twitter import Item
import Twitter

app = Flask(__name__)

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
