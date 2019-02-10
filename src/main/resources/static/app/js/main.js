var app=angular.module("web-shop", ['ngRoute']);

app.controller("mainController", function($scope, $http, $location, $routeParams){
	$scope.user={};
	$scope.user.username="";
	$scope.user.password="";

	var urlLogin="/users/login";
	$scope.login=function(){
		$http.post(urlLogin, $scope.user).then(
			function success(res){
				console.log(res.data);
				localStorage.setItem("user", res.data);
				$location.path("/profile");
			},
			function error(res){
				if(res.status===404){
					alert("No user with given username and password.");
				}
				else{
					alert("Something went wrong.");
				}
				
			}
		);
	};
});

app.controller("moderatorController", function($scope, $http, $location, $routeParams){
	if(localStorage.getItem("user")==null || localStorage.getItem("user")=="" || localStorage.getItem("user")!="moderator"){
		return;
	}
	var urlProducts="/products";
	var urlCategories="/categories";
	var pageNumber=1;
	$scope.products=[];
	var config={params:{}};

	var getProducts=function(){
		config.params.page=pageNumber;
		$http.get(urlProducts, config).then(
			function success(response){
				$scope.products=response.data;
			},
			function error(){
				alert("Could not load products data.");
			}
		);
	};
	getProducts();

		var getCategories=function(){
			$http.get(urlCategories).then(
				function success(res){
					$scope.categories=res.data;
				},
				function error(res){
					alert("Could not load categories data.");
				}
			);
		};
	getCategories();


	$scope.changePage=function(n){
		pageNumber=pageNumber+n;
		getProducts();
	};


	productForUpdate={};
	productForUpdate.id="";
	productForUpdate.price="";
	productForUpdate.category_id="";
	productForUpdate.quantity="";

	$scope.updateProduct=function(id, category, price, quantity){
		productForUpdate.id=id;
		productForUpdate.price=price;
		productForUpdate.category_id=category;
		productForUpdate.quantity=quantity;
		$http.put(urlProducts+"/"+id, productForUpdate).then(
			function success(){
				alert("Product successfully updated.");
				getProducts();
			},
			function error(){
				alert("Could not update product.");
			}
		);
	};

	$scope.deleteProduct=function(id){
		$http.delete(urlProducts+"/"+id).then(
			function success(){
				getProducts();
			},
			function error(){
				alert("Could not delete product.");
			}
		);
	};

	$scope.newProduct={};
	$scope.newProduct.title="";
	$scope.newProduct.category_id="";
	$scope.newProduct.price="";
	$scope.newProduct.quantity="";
	$scope.addProduct=function(){
		$http.post(urlProducts, $scope.newProduct).then(
			function success(){
				alert("Product successfully added.");
				getProducts();
			},
			function error(){
				alert("Could not add product.");
			}
		);
	};

	$scope.logout=function(){
		$http.get("/users/logout").then(
			function success(){
				localStorage.removeItem("user");
				$location.path("/");
			},
			function error(){
				alert("Error happened.");
			}
		);
	};
});

app.controller("userController", function($scope, $http, $location, $routeParams){
	if(localStorage.getItem("user")==null || localStorage.getItem("user")==""){
		return;
	}	
	$scope.empty=true;
	$scope.categorySearch="";
	$scope.min="";
	$scope.max="";
	$scope.sortOrder=1;

	var urlProducts="/products";
	var urlCategories="/categories";
	var urlShoppingCarts="/shopping-carts";
	var pageNumber=1;
	$scope.products=[];
	$scope.categories=[];

	$scope.hideMinMax=function(){
	/*	if($scope.categorySearch==""){
			$scope.empty=true;
		}
		else{
			$scope.empty=false;
		}*/
		$scope.empty=false;
	};

	var getProducts=function(){
		
		var config={params:{}};
		config.params.page=pageNumber;
		if($scope.categorySearch!=""){
			config.params.category_id=$scope.categorySearch;
			if($scope.min!=""){
				config.params.min=$scope.min;
			}
			if($scope.max!=""){
				config.params.max=$scope.max;
			}
			config.params.sortOrder=$scope.sortOrder;
		}

		$http.get(urlProducts, config).then(
			function success(response){
				$scope.products=response.data;
				//console.log(response.data);
			},
			function error(){
				alert("Could not load products data.");
			}
		);
	};
	getProducts();

	var getCategories=function(){
		$http.get(urlCategories).then(
			function success(res){
				$scope.categories=res.data;
			},
			function error(res){
				alert("Could not load categories data.");
			}
		);
	};
	getCategories();

	$scope.search=function(){
		getProducts();
	};
	$scope.reset=function(){
		$scope.empty=true;
		$scope.categorySearch="";
		$scope.min="";
		$scope.max="";
		$scope.sortOrder=1;
		getProducts();
	};

	var pageStart=1;
	$scope.changePage=function(n){
		pageNumber=pageNumber+n;
		pageStart=pageNumber;
		getProducts();
	};	

	$scope.cartItems=[];
	$scope.amount=0.0;
	$scope.addToShoppingCart=function(id, title, price, quantity){
		//console.log(id, title, quantity);
		//console.log($scope.cartItems);
		if(quantity<=0){
			return;
		}
		var exists=false;
		for(i=0; i<$scope.cartItems.length; i++){
			if($scope.cartItems[i].product_id==id){
				exists=true;
				break;
			}
		}		
		if(exists){
			$scope.cartItems[i].quantity+=Number(quantity);
		}
		else{
			var cartItem={};
			cartItem.product_id=id;
			cartItem.title=title;
			cartItem.quantity=Number(quantity);
			$scope.cartItems.push(cartItem);
			
		}
		$scope.amount+=Number(price)*Number(quantity);
		//console.log($scope.cartItems);

	};

	//$scope.cleanArray=[];
	$scope.createShoppingCart=function(){

		$http.post(urlShoppingCarts, $scope.cartItems).then(
			function success(){
				alert("Shopping cart created.");
				$scope.cartItems=[];
				pageNumber=pageStart;
				getProducts();
				//$scope.changePage();
			},
			function error(){
				alert("Error happened while creating shopping cart.");
				$scope.cartItems=[];
				$scope.amount=0.0;
			}
		);
		getShoppingCarts();

	};

	$scope.showShoppingCarts=function(){
		$location.path("/shopping-carts");
	};

	$scope.logout=function(){
		$http.get("/users/logout").then(
			function success(){
				localStorage.removeItem("user");
				$location.path("/");
			},
			function error(){
				alert("Error happened.");
			}
		);
	};
});

app.controller("shoppingCartsController", function($scope, $http, $location, $routeParams){
	if(localStorage.getItem("user")==null || localStorage.getItem("user")==""){
		return;
	}
	var urlShoppingCarts="/shopping-carts";

	$scope.shoppingCarts=[];
	var getShoppingCarts=function(){
		$http.get(urlShoppingCarts).then(
			function success(res){
				$scope.shoppingCarts=res.data;
			},
			function error(){
				alert("Could not get shopping carts.");
			}
		);
	};
	getShoppingCarts();


});

app.config(['$routeProvider', function($routeProvider) {
	$routeProvider
		.when('/', {
			templateUrl : '/app/html/home.html',
		})
		.when('/shopping-carts', {
			templateUrl : '/app/html/shopping-carts.html',
				resolve: {
					loggedIn: function($location){
						if(localStorage.getItem("user")==null || localStorage.getItem("user")==""){
							alert("Nemate dozvolu za pristup ovom sadrzaju.");
							$location.path("/");
						}
					}
				}			
		})
		.when('/mace/:id/informacije', {
			templateUrl : '/app/html/maca-info.html'
		})
		.when('/registracija', {
			templateUrl : '/app/html/registracija.html'
		})
		.when('/login', {
			templateUrl : '/app/html/login.html'
		})
		.when('/moderator', {
			templateUrl : '/app/html/moderator-page.html',
				resolve: {
					loggedInAsAdmin: function($location){
						if(localStorage.getItem("user")!="moderator"){
							//alert("You are not logged in as moderator.");
							$location.path("/");
						}
					}
				}			
		})
		.when('/profile', {
				templateUrl : '/app/html/user-page.html',
				resolve: {
					loggedIn: function($location){
						if(localStorage.getItem("user")==null || localStorage.getItem("user")==""){
							//alert("You are not logged in.");
							$location.path("/");
							return;
						}
						if(localStorage.getItem("user")=="moderator"){
							$location.path("/moderator");
						}
					}
				}
		})	
		.otherwise({
			redirectTo: '/'
		});
}]);