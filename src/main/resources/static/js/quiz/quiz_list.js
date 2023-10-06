
// Get all list items
var listItems = document.getElementsByClassName('list-item');

// Add click event listener to each list item
for (var i = 0 ; i< listItems.length ; i++) {
    listItems[i].addEventListener('click', function() {
        // Show dimmed background and popup when a list item is clicked
        document.getElementById('dimmed-bg').style.display = 'block';
        document.getElementById('popup').style.display = 'block';
    });
}

// Function to close the popup and remove the dimmed background
function closePopup() {
    document.getElementById('dimmed-bg').style.display = 'none';
    document.getElementById('popup').style.display = 'none';
}