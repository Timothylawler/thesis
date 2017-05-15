
import React, { Component } from 'react';
import Dimensions from 'Dimensions';

import {
	Text,
	View,
	NativeModules,
	Platform
} from 'react-native';

import ContactsView from './nativeViews/contactsView';

//	Redux
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';

import * as ContactActions from '../actions/contactPickerActions';

//	Styles
import containerStyle from '../styles/container';

class Contacts extends Component {

	openPicker(){
		var contactPicker = NativeModules.ContactPicker;
		contactPicker.launchPicker((res) => {
			console.log("CONTACTPICKER", res);
			this.props.actions.setPickedContact(res);
		});
	}

	componentDidMount(){
		if(Platform.OS == "ios"){
			this.openPicker();
		}
	}

	render() {
		
		console.log("CONTACTS PROPS: ", this.props);
		var wHeight = Dimensions.get('window').height - 74 ;
		var wWidth = Dimensions.get('window').width;

		return (
			<View style={{height: wHeight, width: wWidth, padding: 8, paddingTop: 65}}>
				<View style={{height: wHeight, width: wWidth}}>
					<ContactsView style={{flex: 1}}/>
					{
						Platform.OS == "ios" &&
						this.props.Contact.name != undefined &&
						<Text>Contact picked: {this.props.Contact.name}</Text>
					}
				</View>
			</View>
		);
	}
}

export default connect((state) => ({
	Contact: state.PickedContactReducer
}), (dispatch) => ({
	actions: bindActionCreators(ContactActions, dispatch)
})
)(Contacts);