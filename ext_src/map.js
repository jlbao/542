function() {
	var category = this.companyName;
	emit(category, {test : this.companyName});
};