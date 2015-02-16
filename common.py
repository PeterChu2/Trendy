class Item:

    def __init__(self, text, imgs, tags, sentiment="neutral"):
        self.text = text
        self.imgs = imgs
        self.tags = tags
        self.sentiment = sentiment
        self.color = "black"

    def serialize(self):
        return {
            "text": self.text,
            "image_url": self.imgs,
            "hash_tags": self.tags,
            "sentiment": self.sentiment,
            "color": self.color,
        }
