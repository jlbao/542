function(key, values){
	var sum = 0;
	values.forEach(function(doc){
		sum += 1;
	});
	return {books: sum};
};