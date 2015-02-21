class Item:

    def __init__(self, text, imgs, tags):
        self.text = text
        self.imgs = imgs
        self.tags = tags
        self.color = "black"

    def serialize(self):
        return {
            "text": self.text,
            "image_url": self.imgs,
            "hash_tags": self.tags,
            "color": self.color,
        }
