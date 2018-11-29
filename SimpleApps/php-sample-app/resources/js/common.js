function objectArrayIndexOf(array, property, value) {
    for(var i = 0; i < array.length; i++) {
        if (array[i][property] === value)
            return i;
    }
    return -1;
}


//formdata to object
$.fn.serializeObject = function(){
    var object = {};
    var formArray = this.serializeArray();

    $.each(formArray, function(){

        if(object[this.name] !== undefined){
            if(!object[this.name].push){
                object[this.name] = [object[this.name]];
            }
            object[this.name].push(this.value || "");
        } else {
            object[this.name] = this.value || "";
        }
    });

    return object;
}
