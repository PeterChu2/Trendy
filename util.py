import string
import operator

non_content = '''!"#$%&\'()*+,-./:;<=>?@[\\]^_`{|}~ \t\n\r\x0b\x0c'''

class Util:
    
    @staticmethod
    def remove_non_ascii(s):
        removed = filter(lambda x: x in string.printable, s)
        removed = removed.strip()

        if all([x in non_content for x in removed]):
            return ""
        else:
            return removed


    @staticmethod
    def extract_hashtags(text):
        items = text.split()
        tags = []

        for item in items:
            if item.startswith("#"):
                tags.append(item.lower())
        
        return tags
    
    @staticmethod
    def sort_dict_by_values(d):
        return sorted(d.items(), key=operator.itemgetter(1))

    
    @staticmethod
    def extract_image_urls(short_urls):
        urls = []

        for u in short_urls:
            urls.append(u["expanded_url"])

        return urls

    @staticmethod
    def extract_text(text):
        items = text.split()
        texts = []

        for item in items:
            if not item.startswith("http"):
                texts.append(item)
        return " ".join(texts)
