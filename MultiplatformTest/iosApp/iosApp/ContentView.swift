import SwiftUI
import shared

struct ContentView: View {
	let greet = Greeting().greet()
    @StateObject var viewModel = IOSContentViewModel()

	var body: some View {
        Text("\(viewModel.state.users.count)")
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
                            users: convertArray(resource.data)
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
        
        private func convertArray<T>(nsArray: NSArray) -> Array<T>? {
            
        }
    }
}
