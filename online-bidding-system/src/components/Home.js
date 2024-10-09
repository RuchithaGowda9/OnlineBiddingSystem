import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import clothingImage from '../resources/images/clothing.jpg'; 
import furnitureImage from '../resources/images/furniture.jpg'; 
import electronicsImage from '../resources/images/electronics.jpg'; 

const Home = () => {
    const featuredItems = [
        {
            id: 1,
            title: 'Clothing',
            description: 'Stylish and comfortable clothing for all seasons.',
            image: clothingImage,
        },
        {
            id: 2,
            title: 'Furniture',
            description: 'Quality furniture to enhance your living space.',
            image: furnitureImage,
        },
        {
            id: 3,
            title: 'Electronics',
            description: 'Latest gadgets and electronics at unbeatable prices.',
            image: electronicsImage,
        },
    ];

    return (
        <div className="text-center mt-5">
            <h1>Welcome to BidMaster, Where Deals Happen</h1>
            <h2 className="mt-4">Featured Items</h2>
            <div className="row mt-4">
                {featuredItems.map(item => (
                    <div className="col-md-4" key={item.id}>
                        <div className="card">
                            <img src={item.image} alt={item.title} className="card-img-top" />
                            <div className="card-body">
                                <h5 className="card-title">{item.title}</h5>
                                <p className="card-text">{item.description}</p>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Home;
