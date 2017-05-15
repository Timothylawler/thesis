
import React, { Component } from 'react';
import {
	Text,
} from 'react-native';

//  style
import textStyle from '../styles/text';

export default class DefaultText extends Component {
	render() {
		return (
			<Text style={textStyle.baseText}>
				{this.props.text}
			</Text>
		);
	}
}



