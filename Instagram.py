from instagram.client import InstagramAPI
from flask import Flask, request
app = Flask(__name__)

access_token = "YOUR_ACCESS_TOKEN"
api = InstagramAPI(client_id='5d9cdd5e12dd4ed7862ff7d0b9867bd7', client_secret='ba7221fefb2547f291af2a98d30b768a')

@app.route('/nearby', methods=['POST'])
def nearby():
  try:
    media_search = api.media_search( count=10, lat=request.form['lat'], lng=-79.396937, distance=10 )
    if(media_search):
      for media in media_search:
        if hasattr(media.caption, 'text'):
          return media.caption.text + "\n"
  except UnicodeEncodeError:
    pass #NOOP

if __name__ == '__main__':
    app.run()
