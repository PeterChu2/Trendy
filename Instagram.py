from instagram.client import InstagramAPI
from flask import Flask, request, jsonify

app = Flask(__name__)

access_token = "YOUR_ACCESS_TOKEN"
api = InstagramAPI(client_id='5d9cdd5e12dd4ed7862ff7d0b9867bd7', client_secret='ba7221fefb2547f291af2a98d30b768a')

@app.route('/nearby', methods=['POST'])
def nearby():

  try:
    media_search = api.media_search( count=10, lat=request.form['lat'], lng=request.form['long'], distance=10 )
    if(media_search):
      items = createJSON(media_search)
      # print items
      return jsonify(items)

  except UnicodeEncodeError:
    pass #NOOP

def createJSON( media_search ):
  i=0
  hashTags = []
  items = {}
  for media in media_search:
    items[i] = {}
    if ( hasattr(media, 'images') & ("standard_resolution" in media.images) ):
      items[i]["image_url"] = media.images['standard_resolution'].url
      if hasattr(media.caption, 'text'):
        items[i]["text"] = media.caption.text
        # GET RID OF UNICODE CHARS HERE
        words = (items[i]["text"]).split(" ")
        for word in words:
          if( "#" in word ):
            hashTags.append(word)
      i=i+1
  items["hash_tags"] = hashTags
  return items

if __name__ == '__main__':
    app.run()
