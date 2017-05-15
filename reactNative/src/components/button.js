/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
	Text,
	View,
	TouchableHighlight,
	StyleSheet
} from 'react-native';

export default class Button extends Component {
	constructor(props) {
		super(props);
		this.state = {
			text: props.text,
			type: props.type
		};
		this.onPressButton = this.onPressButton.bind(this);
	}

	onPressButton() {
		this.setState({ text: "pressed" });
	}

	render() {
		return (
			<TouchableHighlight
				onPress={this.onPressButton}
				style={styles.ios}>
				<Text>
					{this.state.text}
				</Text>
			</TouchableHighlight>
		);
	}
}

const styles = StyleSheet.create({
	ios: {
		backgroundColor: 'rgba(255, 255, 255, 0)',
	},
});


