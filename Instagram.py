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
