function f(key, values){
	var tagStr = "";
	var skills = [];
	var tags = values.tags.split(",");
	tags.forEach(function(tag){
		var tagName = tag.split(":")[0];
		var count = tag.split(":")[1];
		skills.push([tagName, count]);
	});

	skills.sort(function(a, b) {
	 	a = a[1];
	    	b = b[1];
		return b - a;
	});
	
	for(var i = 0; i < 10; i++){
		var tagName = skills[i][0];
		var count = skills[i][1];
		tagStr = tagStr + tagName + ":" + count + ",";
	}
	tagStr = tagStr.substring(0, tagStr.length - 1);
	return {tags: tagStr};
}
