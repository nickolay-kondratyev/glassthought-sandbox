#include <iostream>
#include <GLFW/glfw3.h>

int main() {
    std::cout << "Hi World!\n";

    GLFWwindow* window;
    int width, height;

    if( !glfwInit() )
    {
      fprintf( stderr, "Failed to initialize GLFW\n" );
      exit( EXIT_FAILURE );
    }

    window = glfwCreateWindow( 300, 300, "Gears", NULL, NULL );
    if (!window)
    {
        fprintf( stderr, "Failed to open GLFW window\n" );
        glfwTerminate();
        exit( EXIT_FAILURE );
    }

    while( !glfwWindowShouldClose(window) )
    {

        // Swap buffers
        // glfwSwapBuffers(window);
        glfwPollEvents();
    }

    // Terminate GLFW
    glfwTerminate();

    // Exit program
    exit( EXIT_SUCCESS );

    return 0;
}
