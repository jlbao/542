function() {
	var skills = new Array();
	var tagStr = "";
	this.tags.forEach(
		function(tag){
			skills[tag] = 1;
		}
	);
	
	for(var tagName in skills){
		if(skills.hasOwnProperty(tagName)){
			tagStr = tagStr + tagName + ":" + skills[tagName] + ",";
		}
	}

	tagStr = tagStr.substring(0, tagStr.length - 1);
	emit(this.companyName, {tags: tagStr});
}
