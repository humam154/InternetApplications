import React from "react";
import { Link } from "react-router-dom";

const Home = () => {
    return (
        <div className="flex justify-center items-center h-screen bg-gray-100">
            <Link to="/login" className="group relative">
                <div className="relative w-80 h-24 opacity-90 overflow-hidden rounded-xl bg-black z-10">
                    {/* Hover Effect */}
                    <div className="absolute z-10 -translate-x-44 group-hover:translate-x-[30rem] ease-in transition-all duration-700 h-full w-56 bg-gradient-to-r from-gray-500 to-white/10 opacity-30 -skew-x-12"></div>

                    {/* Inner Button */}
                    <div className="absolute flex items-center justify-center text-white z-[1] opacity-90 rounded-2xl inset-0.5 bg-black">
                        <button
                            className="font-semibold text-2xl h-full opacity-90 w-full px-16 py-4 rounded-xl bg-black text-white"
                            title="Enter"
                        >
                            Enter
                        </button>
                    </div>

                    {/* Blur Effect */}
                    <div className="absolute duration-1000 group-hover:animate-spin w-full h-[100px] bg-gradient-to-r from-green-500 to-yellow-500 blur-[30px]"></div>
                </div>
            </Link>
        </div>
    );
};

export default Home;
