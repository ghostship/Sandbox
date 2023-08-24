//
//  ContentUiState.swift
//  iosApp
//
//  Created by Scott Keller on 8/24/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

struct ContentUiState {
    var isLoading: Bool = false
    var errorMessage: String? = nil
    var users: Array<User> = Array()
}
