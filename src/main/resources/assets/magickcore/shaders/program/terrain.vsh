#version 150 compatibility

out vec3 position;
out vec4 lcolor;
out float intens;
out float dist2Obj;

struct Light
{
    vec4 color;
    vec3 position;
    float radius;
};

uniform int chunkX;
uniform int chunkY;
uniform int chunkZ;
uniform int lightCount;
uniform Light lights[256];

void main()
{
    vec4 pos = gl_ModelViewProjectionMatrix * gl_Vertex;
    position = gl_Vertex.xyz + vec3(chunkX, chunkY, chunkZ);
    gl_TexCoord[0] = gl_TextureMatrix[0] * gl_MultiTexCoord0;
    gl_TexCoord[2] = gl_TextureMatrix[2] * gl_MultiTexCoord2;
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    //gl_Position = ftransform();
    gl_FrontColor = gl_Color;
    lcolor = vec4(0, 0, 0, 1.0f);
    dist2Obj = length(gl_Position);

    float sumR = 0;
    float sumG = 0;
    float sumB = 0;
    float count = 0;
    float maxIntens = 0;
    float totalIntens = 0;
    for(int i = 0; i < lightCount; i++)
    {
        Light l = lights[i];
        float radius = pow(l.radius, 2);
        float intensity = pow(max(0, 1.0f - distance(l.position, position) / l.radius), 2);
        totalIntens += intensity;
        maxIntens = max(maxIntens, intensity);
    }
    for(int i = 0; i < lightCount; i++)
    {
        Light l = lights[i];
        float radius = pow(l.radius, 2);
        float intensity = pow(max(0, 1.0f - distance(l.position, position) / l.radius), 2);
        sumR += l.color.r * (intensity / totalIntens);
        sumG += l.color.g * (intensity / totalIntens);
        sumB += l.color.b * (intensity / totalIntens);
    }
    lcolor = vec4(max(sumR * 1.5f, 0.0f), max(sumG * 1.5f, 0.0f), max(sumB * 1.5f, 0.0f), 1.0f);
    intens = min(1.0f, maxIntens);
}
