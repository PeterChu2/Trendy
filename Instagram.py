from instagram.client import InstagramAPI
from flask import Flask, request
app = Flask(__name__)

access_token = "YOUR_ACCESS_TOKEN"
api = InstagramAPI(client_id='5d9cdd5e12dd4ed7862ff7d0b9867bd7', client_secret='ba7221fefb2547f291af2a98d30b768a')

@app.route('/nearby', methods=['POST'])
def nearby():
  items = []
  try:
    media_search = api.media_search( count=10, lat=request.form['lat'], lng=request.form['long'], distance=10 )
    if(media_search):
      for media in media_search:
        if hasattr(media.caption, 'text'):
          items.append( media.caption.text + "\n" )
          print media.caption.text
      return "\n".join(items)

  except UnicodeEncodeError:
    pass #NOOP

def returnHashTags( input ):
  strings = input.split(' ')
  for s in strings:
    if("#" in s):
      return s

if __name__ == '__main__':
    app.run()
