function(key, values){
	
	var skills = new Array();
	var tagStr = "";
	values.forEach(function(doc){
		var tags = doc.tags.split(",");
		tags.forEach(function(tag){
			var tagName = tag.split(":")[0];
			var count = tag.split(":")[1];
			if(skills[tagName] == undefined){
				skills[tagName] = 1;
			}else{
				skills[tagName] = parseInt(skills[tagName]) + parseInt(count);
			}
		});
	});

	for(var tagName in skills){
		if(skills.hasOwnProperty(tagName)){
			tagStr = tagStr + tagName + ":" + skills[tagName] + ",";
		}
	}
	tagStr = tagStr.substring(0, tagStr.length - 1);
	return {tags: tagStr};

}
