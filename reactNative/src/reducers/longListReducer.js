const initialState = []

export default function LongListReducer(state = initialState, action) {
	switch (action.type) {
		case "ADDITEM":
			//	Concat is not mutating
			//console.log("ADDITEM: ", state);
			//console.log("ADDITEMACTION: ", action);
			let id = new Date().toString();
			return state.concat([
				{
					_id: id,
					firstName: action.firstName, 
					lastName: action.lastName
				}
			]);
			
		case "REMOVEITEM": 
			//let tempDelete = state.filter((item) => item._id !== action.rowID);
			//return tempDelete;
			return[
				...state.slice(0, action.rowID),
				...state.slice(action.rowID + 1)
			];
			
			

		case "FILL":
			//console.log("FILL: ", action.data );
			return action.data;
			

		case "EDITITEM": 
			/*	concat the slice up to the index to update
					Append the new item
					concat the rest of the state after the new item
			*/
			return [
				...state.slice(0, action.rowID),
				{_id: state[action.rowID]._id + "new",firstName: "new FirstName", lastName: "new lastName"},
				...state.slice(action.rowID + 1)
			];
			
		default:
			return state;
	}
};