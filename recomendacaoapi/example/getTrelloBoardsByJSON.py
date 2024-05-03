import json
from PIL import Image, ImageDraw, ImageFont

# Load the Trello export
with open("trello_boards.json", "r") as file:
    trello_data = json.load(file)

# Extract the lists and cards from the Trello data
lists_data = trello_data['lists']
cards_data = trello_data['cards']

# Define the desired columns and some visualization constants
desired_columns = ["BACKLOG DO PRODUTO", "EM ANDAMENTO", "FEITO", "REVISÃO", "CONCLUIDO", "EXCLUÍDO"]
image_width = 1500
image_height = 800
column_width = image_width // len(desired_columns)
font = ImageFont.load_default()
card_height = 40
card_margin = 5
card_y_start = 40

def split_text_into_two_lines(text, max_width):
    """Splits a text into two lines if it exceeds the max_width."""
    if draw.textsize(text, font=font)[0] <= max_width:
        return text, ""
    
    words = text.split()
    line1 = ""
    line2 = ""

    # Build the first line
    while words and draw.textsize(line1 + words[0], font=font)[0] <= max_width:
        line1 += (words.pop(0) + " ")

    # Remaining words for the second line
    line2 = " ".join(words)

    return line1.strip(), line2.strip()

def draw_card(column_index, card_name, position):
    x_start = column_index * column_width + card_margin
    x_end = (column_index + 1) * column_width - card_margin
    y_start = card_y_start + position * (card_height + card_margin)
    y_end = y_start + card_height
    
    # Draw the card rectangle
    draw.rectangle([x_start, y_start, x_end, y_end], fill="lightblue", outline="black")
    
    # Split card name into two lines if necessary
    line1, line2 = split_text_into_two_lines(card_name, column_width - 2 * card_margin)
    
    # Vertical positioning based on whether there's a second line
    if line2:
        text_y_offset = card_height / 4
    else:
        text_y_offset = card_height / 2

    # Draw the first line
    text_width, text_height = draw.textsize(line1, font=font)
    text_x = x_start + (column_width - text_width - 2 * card_margin) / 2
    text_y = y_start + text_y_offset - text_height
    draw.text((text_x, text_y), line1, fill="black", font=font)

    # Draw the second line (if it exists)
    if line2:
        text_width, text_height = draw.textsize(line2, font=font)
        text_x = x_start + (column_width - text_width - 2 * card_margin) / 2
        text_y = y_start + 3 * text_y_offset - text_height
        draw.text((text_x, text_y), line2, fill="black", font=font)

# Create the blank image and a drawing context
image = Image.new("RGB", (image_width, image_height), "white")
draw = ImageDraw.Draw(image)

# Draw the columns and their titles
for index, column_name in enumerate(desired_columns):
    x_start = index * column_width
    x_end = (index + 1) * column_width
    text_width, text_height = draw.textsize(column_name, font=font)
    text_x = x_start + (column_width - text_width) / 2
    text_y = 10
    draw.text((text_x, text_y), column_name, fill="black", font=font)
    draw.line((x_end, 0, x_end, image_height), fill="gray")

# Draw each card in the respective column
list_name_to_index = {name: index for index, name in enumerate(desired_columns)}
for card in cards_data:
    list_name = next((lst['name'] for lst in lists_data if lst['id'] == card['idList']), None)
    if list_name in list_name_to_index:
        column_index = list_name_to_index[list_name]
        position = sum(1 for c in cards_data if c['idList'] == card['idList'] and c['pos'] <= card['pos'])
        draw_card(column_index, card['name'], position)

# Show the final Kanban image
image.show()
