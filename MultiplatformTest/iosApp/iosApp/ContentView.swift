import SwiftUI
import shared

struct ContentView: View {
	let greet = Greeting().greet()
    @StateObject var viewModel = IOSContentViewModel()

	var body: some View {
        VStack {
            List(viewModel.state.users, id: \.id) {user in
                RowView(user: user)
            }.frame(height: .infinity)
            
            Button("Fetch Users") {}
            Button("Create New User") {}
        }
        .onAppear(perform: {
            viewModel.getUsers()
        })
        .onDisappear(perform: {
            viewModel.dispose()
        })
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}

struct RowView: View {
    var user: User
    
    var body: some View {
        HStack {
            Text("\(user.id) \(user.fullName)")
            
            Spacer()
            
            Button {} label: {
                Image(systemName: "pencil")
            }
            
            Spacer().frame(width: 20)
            
            Button {} label: {
                Image(systemName: "trash")
            }
        }
    }
    
}

struct RowView_Previews: PreviewProvider {
    static var previews: some View {
        var user = User(
            id: 1,
            firstName: "Bob",
            lastName: "Testman")
        
        RowView(user: user)
    }
}

extension ContentView {
    @MainActor class IOSContentViewModel: ObservableObject {
        private let repository: SampleRepository
        
        @Published var state: ContentUiState = ContentUiState()
        
        init() {
            self.repository = SampleRepositoryImpl()
        }
        
        private var getUsersHandle: Kotlinx_coroutines_coreDisposableHandle?
        
        func getUsers() {
            getUsersHandle = repository.getUsers().subscribe(
                onCollect: { resource in
                    if (resource is ResourceLoading) {
                        self.state = ContentUiState(
                            isLoading: true
                        )
                        
                    } else if let resource = resource as? ResourceSuccess {
                        self.state = ContentUiState(
                            isLoading: false,
                            users: self.convertArray(nsArray: resource.data)
                        )
                        
                    } else if (resource is ResourceError) {
                        self.state = ContentUiState(
                            isLoading: false,
                            errorMessage: "Error happened"
                        )
                    }
                }
            )
        }
        
        // Removes the listener
        func dispose() {
            getUsersHandle?.dispose()
        }
        
        private func convertArray<T>(nsArray: NSArray) -> Array<T> {
            return nsArray as! [T]
        }
    }
}
