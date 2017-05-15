export function addItem(data){
	return{
		type: "ADDITEM",
		firstName: data.firstName,
		lastName: data.lastName
	}
}

export function removeItem(rowID){
	return{
		type: "REMOVEITEM",
		rowID: rowID
	}
}

export function editItem(rowID){
	return{
		type: "EDITITEM",
		rowID: rowID
	}
}

export function fill(data){
	return{
		type: "FILL",
		data: data
	}
}